"use client";

import { useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import {
  getProjects,
  createProject,
  deleteProject,
} from "@/services/projectService";
import useAuthToken from "@/hooks/useAuthToken";
import ProjectModal from "@/components/ProjectModal";
import { showToast } from "@/utils/toastHelper";
import { showConfirmationDialog } from "@/utils/confirmDialog";
import { useLoadingStore } from "@/store/useLoadingStore";
import { Project } from "@/types/Global";



export default function DashboardPage() {
  const router = useRouter();
  const token = useAuthToken();

  const [projects, setProjects] = useState<Project[]>([]);
  const [showModal, setShowModal] = useState(false);

  const { startLoading, stopLoading } = useLoadingStore();

  const fetchProjects = useCallback(async () => {
    if (!token) return;

    try {
      startLoading();
      const data = await getProjects(token);
      setProjects(data);
    } catch (error) {
      console.error("Error al cargar proyectos:", error);
      showToast("error", "Error al cargar proyectos");
    } finally {
      stopLoading();
    }
  }, [token, startLoading, stopLoading]);

  useEffect(() => {
    if (token === null) return;
    if (!token) {
      router.push("/login");
    } else {
      fetchProjects();
    }
  }, [token, router, fetchProjects]);

  const handleCreate = async (name: string, description: string) => {
    if (!token) return;

    try {
      startLoading();
      await createProject({ name, description }, token);
      await fetchProjects();
      setShowModal(false);
      showToast("success", "Proyecto creado correctamente");
    } catch (error) {
      console.error("Error al crear proyecto:", error);
      showToast("error", "Error al crear el proyecto");
    } finally {
      stopLoading();
    }
  };

  const handleDeleteProject = async (projectId: number) => {
    if (!token) return;

    const confirmed = await showConfirmationDialog(
      "¿Estás seguro?",
      "¡Esta acción no se puede deshacer!",
      "Sí, eliminar"
    );

    if (!confirmed) return;

    try {
      startLoading();
      await deleteProject(projectId, token);
      await fetchProjects();
      showToast("success", "Proyecto eliminado correctamente");
    } catch (error) {
      console.error("Error al eliminar proyecto:", error);
      showToast("error", "Error al eliminar el proyecto");
    } finally {
      stopLoading();
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Mis Proyectos</h1>
        <button
          onClick={() => setShowModal(true)}
          className="bg-black text-white px-4 py-2 rounded"
        >
          + Nuevo Proyecto
        </button>
      </div>

      {projects.length === 0 ? (
        <p className="text-gray-500">No tienes proyectos aún.</p>
      ) : (
        <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {projects.map((project) => (
            <li
              key={project.id}
              className="border p-4 rounded shadow-sm bg-white relative"
            >
              <h2
                className="font-semibold cursor-pointer hover:underline"
                onClick={() => router.push(`/project/${project.id}`)}
              >
                {project.name}
              </h2>
              <p className="text-sm text-gray-600 mb-2">
                {project.description}
              </p>

              <button
                onClick={() => handleDeleteProject(project.id)}
                className="text-xs text-red-600 hover:underline absolute top-2 right-2"
              >
                Eliminar
              </button>
            </li>
          ))}
        </ul>
      )}

      {showModal && (
        <ProjectModal
          onClose={() => setShowModal(false)}
          onSave={handleCreate}
        />
      )}
    </div>
  );
}
