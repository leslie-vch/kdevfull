// src/hooks/useUserRole.ts
import { useEffect, useState } from "react";

export function useUserRole(defaultRole = "USER") {
  const [userRole, setUserRole] = useState<string>(defaultRole);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        const payloadBase64 = token.split(".")[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));
        if (decodedPayload?.role) {
          setUserRole(decodedPayload.role);
        }
      } catch (err) {
        console.error("Error al decodificar el token:", err);
      }
    }
  }, []);

  return userRole;
}
