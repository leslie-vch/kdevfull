import Swal from 'sweetalert2';

export const showToast = (
  icon: 'success' | 'error' | 'warning' | 'info',
  title: string
) => {
  Swal.fire({
    toast: true,
    position: 'top-end',
    icon,
    title,
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
  });
};
