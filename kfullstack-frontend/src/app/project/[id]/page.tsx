"use client";

import { useEffect, useState, useCallback } from "react";
import { useParams, useRouter } from "next/navigation";
import { getProjectById, updateProject } from "@/services/projectService";
import {
  getTasksByProjectId,
  createTask,
  deleteTask,
  updateTask,
} from "@/services/taskService";
import ProjectModal from "@/components/ProjectModal";
import TaskModal from "@/components/TaskModal";
import { showToast } from "@/utils/toastHelper";
import { showConfirmationDialog } from "@/utils/confirmDialog";
import { useLoadingStore } from "@/store/useLoadingStore";
import { Project, Task } from "@/types/Global";
import { useUserRole } from "@/hooks/useUserRole";

export default function ProjectDetailPage() {
  const { id } = useParams();
  const router = useRouter();

  const [project, setProject] = useState<Project | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showTaskModal, setShowTaskModal] = useState(false);
  const [showEditTaskModal, setShowEditTaskModal] = useState(false);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [selectedStatus, setSelectedStatus] = useState<string>("ALL");
  const [selectedSort, setSelectedSort] = useState<"ASC" | "DESC">("ASC");

  const { isLoading, startLoading, stopLoading } = useLoadingStore();



  const userRole = useUserRole();

  const fetchData = useCallback(async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      router.push("/login");
      return;
    }

    startLoading();
    try {
      const projectData = await getProjectById(id as string, token);
      const taskData = await getTasksByProjectId(
        id as string,
        token,
        selectedStatus !== "ALL" ? selectedStatus : undefined,
        selectedSort
      );
      setProject(projectData);
      setTasks(taskData);
    } catch (error) {
      showToast("error", "No se pudo cargar el proyecto");
      router.push("/dashboard");
    } finally {
      stopLoading();
    }
  }, [id, router, selectedStatus, selectedSort, startLoading, stopLoading]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const refreshTasks = async () => {
    const token = localStorage.getItem("token");
    if (!token || !id) return;
    const updatedTasks = await getTasksByProjectId(
      id as string,
      token,
      selectedStatus !== "ALL" ? selectedStatus : undefined,
      selectedSort
    );
    setTasks(updatedTasks);
  };

  const handleUpdateProject = async (name: string, description: string) => {
    const token = localStorage.getItem("token");
    if (!token || !id) return;

    startLoading();
    try {
      const updated = await updateProject(
        id as string,
        { name, description },
        token
      );
      setProject(updated);
      showToast("success", "Proyecto actualizado");
      setShowEditModal(false);
    } catch (err) {
      showToast("error", "Error actualizando proyecto");
    } finally {
      stopLoading();
    }
  };

  const handleCreateTask = async (task: any) => {
    const token = localStorage.getItem("token");
    if (!token || !id) return;

    startLoading();
    try {
      await createTask(
        {
          ...task,
          projectId: parseInt(id as string),
        },
        token
      );
      showToast("success", "Tarea creada exitosamente");
      await refreshTasks();
      setShowTaskModal(false);
    } catch (err) {
      showToast("error", "Error al crear tarea");
    } finally {
      stopLoading();
    }
  };

  const handleUpdateTask = async (taskData: any) => {
    const token = localStorage.getItem("token");
    if (!token || !id || !editingTask) return;

    startLoading();
    try {
      await updateTask(editingTask.id, taskData, token);
      showToast("success", "Tarea actualizada");
      await refreshTasks();
      setShowEditTaskModal(false);
      setEditingTask(null);
    } catch (err) {
      showToast("error", "Error actualizando tarea");
    } finally {
      stopLoading();
    }
  };

  const handleDeleteTask = async (taskId: number) => {
    const token = localStorage.getItem("token");
    if (!token || !id) return;

    const confirmed = await showConfirmationDialog(
      "¿Estás seguro?",
      "¡Esta acción no se puede deshacer!"
    );
    if (!confirmed) return;

    startLoading();
    try {
      await deleteTask(taskId, token);
      showToast("success", "Tarea eliminada");
      await refreshTasks();
    } catch (err) {
      showToast("error", "Error al eliminar tarea");
    } finally {
      stopLoading();
    }
  };

  if (!project) return null;

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">{project.name}</h1>
        <button
          onClick={() => setShowEditModal(true)}
          className="text-sm bg-black text-white px-4 py-2 rounded"
        >
          Editar Proyecto
        </button>
      </div>

      <p className="mb-6 text-gray-600">{project.description}</p>

      <div className="flex justify-between items-center mb-4">
        <div className="flex items-center gap-4">
          <select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
            className="border rounded px-2 py-1 text-sm"
          >
            <option value="ALL">Todas</option>
            <option value="PENDING">Pendiente</option>
            <option value="IN_PROGRESS">En progreso</option>
            <option value="DONE">Finalizada</option>
          </select>

          <select
            value={selectedSort}
            onChange={(e) => setSelectedSort(e.target.value as "ASC" | "DESC")}
            className="border rounded px-2 py-1 text-sm"
          >
            <option value="ASC">Más antiguas primero</option>
            <option value="DESC">Más recientes primero</option>
          </select>
        </div>

        <button
          onClick={() => setShowTaskModal(true)}
          className="bg-black text-white px-4 py-1 rounded text-sm"
        >
          + Nueva Tarea
        </button>
      </div>

      {tasks.length === 0 ? (
        <p className="text-gray-500">
          No hay tareas para el estado seleccionado.
        </p>
      ) : (
        <ul className="space-y-3">
          {tasks.map((task) => (
            <li
              key={task.id}
              className="border p-4 rounded-md bg-white shadow-sm relative"
            >
              <button
                onClick={() => {
                  setEditingTask(task);
                  setShowEditTaskModal(true);
                }}
                className="absolute top-2 right-16 text-xs text-blue-600 hover:underline"
              >
                Editar
              </button>

              <button
                onClick={() => handleDeleteTask(task.id)}
                className="absolute top-2 right-2 text-xs text-red-600 hover:underline"
              >
                Eliminar
              </button>

              <h3 className="font-semibold">{task.title}</h3>
              <p className="text-sm text-gray-600">{task.description}</p>
              <div className="mt-2 text-sm text-gray-500">
                Estado: <strong>{task.status}</strong> | Vence:{" "}
                {new Date(task.dueDate).toLocaleDateString()}
              </div>
              {task.assignedTo && (
                <div className="text-sm text-gray-400 mt-1">
                  Asignado a: <strong>{task.assignedTo.username}</strong>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}

      {showEditModal && (
        <ProjectModal
          onClose={() => setShowEditModal(false)}
          onSave={handleUpdateProject}
          initialName={project.name}
          initialDescription={project.description}
        />
      )}

      {showTaskModal && (
        <TaskModal
          onClose={() => setShowTaskModal(false)}
          onSave={handleCreateTask}
          userRole={userRole}
        />
      )}

      {showEditTaskModal && editingTask && (
        <TaskModal
          onClose={() => {
            setShowEditTaskModal(false);
            setEditingTask(null);
          }}
          onSave={handleUpdateTask}
          userRole={userRole}
          initialData={{
            title: editingTask.title,
            description: editingTask.description,
            dueDate: editingTask.dueDate.split("T")[0],
            status: editingTask.status,
            assignedTo: editingTask.assignedTo ?? undefined,
          }}
        />
      )}
    </div>
  );
}
