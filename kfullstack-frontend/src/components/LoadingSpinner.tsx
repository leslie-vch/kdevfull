
"use client";

import { useLoadingStore } from "@/store/useLoadingStore";

export default function LoadingSpinner() {
  const isLoading = useLoadingStore((state) => state.isLoading);

  if (!isLoading) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div className="w-16 h-16 border-4 border-white border-t-black rounded-full animate-spin"></div>
    </div>
  );
}
