import axios from "axios";

const API_URL = "http://localhost:8080/kfullstack/users";

export const getAllUsers = async (token: string) => {
  const response = await axios.get(API_URL, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};

export const createUsers = async (token: string) => {
  const response = await axios.post(
    `${API_URL}/random`,
    {},
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  return response.data;
};
