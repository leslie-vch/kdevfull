"use client";

import { getAllUsers } from "@/services/userService";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";

type TaskStatus = "PENDING" | "IN_PROGRESS" | "DONE";

type Props = {
  onSave: (task: {
    title: string;
    description: string;
    dueDate: string;
    status: TaskStatus;
    assignedToId: number | null;
    projectId?: number;
  }) => void;
  onClose: () => void;
  userRole: string;
  initialData?: {
    title: string;
    description: string;
    dueDate: string;
    status: TaskStatus;
    assignedTo?: {
      id: number;
      username: string;
    };
  };
};

type FormData = {
  title: string;
  description: string;
  dueDate: string;
  status: TaskStatus;
  assignedToId: string; // mantendremos string para el select
};

export default function TaskModal({
  onSave,
  onClose,
  userRole,
  initialData,
}: Props) {
  const [users, setUsers] = useState<{ id: number; username: string }[]>([]);

  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm<FormData>({
    defaultValues: {
      title: initialData?.title || "",
      description: initialData?.description || "",
      dueDate: initialData?.dueDate || "",
      status: initialData?.status || "PENDING",
      assignedToId: initialData?.assignedTo?.id.toString() || "",
    },
  });

  useEffect(() => {
    if (userRole === "ADMIN") {
      const fetchUsers = async () => {
        const token = localStorage.getItem("token");
        if (!token) return;
        try {
          const data = await getAllUsers(token);
          setUsers(data);
        } catch (err) {
          console.error("Error al cargar usuarios:", err);
        }
      };
      fetchUsers();
    }
  }, [userRole]);

  const onSubmit = (data: FormData) => {
    onSave({
      title: data.title,
      description: data.description,
      dueDate: data.dueDate,
      status: data.status,
      assignedToId: data.assignedToId ? parseInt(data.assignedToId) : null,
    });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg w-96">
        <h2 className="text-xl font-bold mb-4">
          {initialData ? "Editar Tarea" : "Nueva Tarea"}
        </h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <input
            type="text"
            placeholder="Título"
            className="w-full border p-2 mb-1 rounded"
            {...register("title", { required: "El título es requerido" })}
          />
          {errors.title && (
            <p className="text-red-500 text-sm mb-2">{errors.title.message}</p>
          )}

          <textarea
            placeholder="Descripción"
            rows={3}
            className="w-full border p-2 mb-1 rounded"
            {...register("description", {
              required: "La descripción es requerida",
            })}
          />
          {errors.description && (
            <p className="text-red-500 text-sm mb-2">
              {errors.description.message}
            </p>
          )}

          <input
            type="date"
            className="w-full border p-2 mb-1 rounded"
            {...register("dueDate", { required: "La fecha es obligatoria" })}
          />
          {errors.dueDate && (
            <p className="text-red-500 text-sm mb-2">
              {errors.dueDate.message}
            </p>
          )}

          <select
            className="w-full border p-2 mb-3 rounded"
            {...register("status")}
          >
            <option value="PENDING">Pendiente</option>
            <option value="IN_PROGRESS">En progreso</option>
            <option value="DONE">Finalizada</option>
          </select>

          {userRole === "ADMIN" && (
            <select
              className="w-full border p-2 mb-3 rounded"
              {...register("assignedToId")}
            >
              <option value="">-- Asignar a usuario --</option>
              {users.map((u) => (
                <option key={u.id} value={u.id}>
                  {u.username}
                </option>
              ))}
            </select>
          )}

          <div className="flex justify-end gap-2 mt-3">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded border border-gray-400 text-gray-700"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-black text-white rounded"
            >
              {initialData ? "Actualizar" : "Crear"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
