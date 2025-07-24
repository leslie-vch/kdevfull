"use client";

import { FormData, Props } from "@/types/Global";
import { useForm } from "react-hook-form";



export default function ProjectModal({
  onSave,
  onClose,
  initialName = "",
  initialDescription = "",
}: Props) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormData>({
    defaultValues: {
      name: initialName,
      description: initialDescription,
    },
  });

  const onSubmit = (data: FormData) => {
    onSave(data.name, data.description);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg w-96">
        <h2 className="text-xl font-bold mb-4">Editar Proyecto</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <input
            type="text"
            placeholder="Nombre"
            className="w-full border p-2 mb-1 rounded"
            {...register("name", { required: "El nombre es requerido" })}
          />
          {errors.name && (
            <p className="text-red-500 text-sm mb-2">{errors.name.message}</p>
          )}

          <textarea
            placeholder="Descripción"
            className="w-full border p-2 mb-1 rounded"
            rows={3}
            {...register("description", { required: "La descripción es requerida" })}
          />
          {errors.description && (
            <p className="text-red-500 text-sm mb-2">
              {errors.description.message}
            </p>
          )}

          <div className="flex justify-end gap-2 mt-4">
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
              Guardar Cambios
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
