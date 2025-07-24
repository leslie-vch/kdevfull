
import axios from "axios";

const API_URL = "http://localhost:8080/kfullstack/projects";

export const getProjects = async (token: string) => {
  const response = await axios.get(API_URL, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const createProject = async (
  project: { name: string; description: string },
  token: string
) => {
  const response = await axios.post(API_URL, project, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const getProjectById = async (projectId: string, token: string) => {
  const response = await axios.get(`${API_URL}/${projectId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const updateProject = async (
  projectId: string,
  project: { name: string; description: string },
  token: string
) => {
  const response = await axios.put(`${API_URL}/${projectId}`, project, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const deleteProject = async (projectId: number, token: string) => {
  const response = await axios.delete(`${API_URL}/${projectId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};
