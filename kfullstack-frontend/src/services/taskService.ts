import axios from "axios";

const API_URL = "http://localhost:8080/kfullstack/tasks";

export const getTasksByProjectId = async (
  projectId: string,
  token: string,
  status?: string,
  sortBy?: "ASC" | "DESC"
) => {
  const response = await axios.get(`${API_URL}/project/${projectId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
    params: {
      ...(status && { status }),
      ...(sortBy && { sort: sortBy }),
    },
  });

  return response.data;
};

export const createTask = async (
  task: {
    title: string;
    description: string;
    dueDate: string;
    status: "PENDING" | "IN_PROGRESS" | "DONE";
    projectId: number;
  },
  token: string
) => {
  const response = await axios.post(`${API_URL}`, task, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const deleteTask = async (taskId: number, token: string) => {
  const response = await axios.delete(`${API_URL}/${taskId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const updateTask = async (
  taskId: number,
  task: {
    title: string;
    description: string;
    dueDate: string;
    status: "PENDING" | "IN_PROGRESS" | "DONE";
    assignedToId?: number | null;
  },
  token: string
) => {
  const response = await axios.put(`${API_URL}/${taskId}`, task, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};
