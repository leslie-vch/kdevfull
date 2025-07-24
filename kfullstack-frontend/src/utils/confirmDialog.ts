import Swal from "sweetalert2";

export const showConfirmationDialog = async (
  title: string = "¿Estás seguro?",
  text: string = "¡Esta acción no se puede deshacer!",
  confirmButtonText: string = "Sí, eliminar"
): Promise<boolean> => {
  const result = await Swal.fire({
    title,
    text,
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#aaa",
    confirmButtonText,
    cancelButtonText: "Cancelar",
  });

  return result.isConfirmed;
};
