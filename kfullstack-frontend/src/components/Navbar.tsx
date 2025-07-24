"use client";

import { createUsers } from "@/services/userService";
import { showToast } from "@/utils/toastHelper";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Navbar() {
  const router = useRouter();
  const [userRole, setUserRole] = useState("USER");
  const { id } = useParams();

  const handleCreateUser = async () => {
    const token = localStorage.getItem("token");
    if (!token || !id) return;

    try {
      await createUsers(token);
      showToast("success", "Tarea creada exitosamente");
    } catch (err) {
      showToast("error", "Error al crear tarea");
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const payloadBase64 = token.split(".")[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));
        setUserRole(decodedPayload.role);
      } catch (err) {
        console.error("Error al decodificar el token:", err);
      }
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    router.push("/login");
  };

  const handleGoBack = () => {
    router.back();
  };

  return (
    <nav className="bg-black text-white px-6 py-4 flex justify-between items-center">
      <h1 className="text-lg font-bold">KFullstack</h1>

      <div className="flex items-center gap-4">
        {userRole && (
          <span className="flex items-center gap-2 text-sm text-white bg-gray-700 px-3 py-1 rounded-full shadow-inner">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-4 w-4 text-yellow-300"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M13 16h-1v-4h-1m1-4h.01M12 12h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
            Rol:{" "}
            <span className="font-medium capitalize">
              {userRole.toLowerCase()}
            </span>
          </span>
        )}

        <button
          onClick={handleGoBack}
          className="bg-gray-700 text-white px-4 py-1 rounded hover:bg-gray-600"
        >
          Retroceder
        </button>

        <button
          onClick={handleCreateUser}
          className="bg-blue-600 text-white px-4 py-1 rounded hover:bg-blue-500"
        >
          Crear Usuario
        </button>

        <button
          onClick={handleLogout}
          className="bg-white text-black px-4 py-1 rounded hover:bg-gray-200"
        >
          Cerrar sesión
        </button>
      </div>
    </nav>
  );
}
