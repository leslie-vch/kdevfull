"use client";

import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";
import axios from "axios";
import { showToast } from "@/utils/toastHelper";
import { useLoadingStore } from "@/store/useLoadingStore";
import { LoginForm } from "@/types/Global";



export default function LoginPage() {
  const router = useRouter();
  const { startLoading, stopLoading } = useLoadingStore();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginForm>();

  const onSubmit = async (data: LoginForm) => {
    try {
      startLoading();

      const res = await axios.post("http://localhost:8080/kfullstack/auth/login", data);
      localStorage.setItem("token", res.data.token);

      showToast("success", "Inicio de sesión exitoso");
      router.push("/dashboard");
    } catch {
      showToast("error", "Credenciales incorrectas");
    } finally {
      stopLoading();
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="bg-white p-6 rounded-lg shadow-md w-full max-w-sm"
      >
        <h2 className="text-2xl font-bold mb-6 text-center">Iniciar Sesión</h2>

        {/* Usuario */}
        <div className="mb-4">
          <input
            type="text"
            placeholder="Usuario"
            {...register("username", {
              required: "El usuario es obligatorio",
              minLength: { value: 3, message: "Mínimo 3 caracteres" },
            })}
            className="w-full p-2 border rounded"
          />
          {errors.username && (
            <p className="text-red-500 text-sm mt-1">{errors.username.message}</p>
          )}
        </div>

        {/* Contraseña */}
        <div className="mb-4">
          <input
            type="password"
            placeholder="Contraseña"
            {...register("password", {
              required: "La contraseña es obligatoria",
              minLength: { value: 4, message: "Mínimo 4 caracteres" },
            })}
            className="w-full p-2 border rounded"
          />
          {errors.password && (
            <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>
          )}
        </div>

        <button
          type="submit"
          className="w-full bg-black text-white p-2 rounded hover:bg-gray-800"
        >
          Ingresar
        </button>
      </form>
    </div>
  );
}
