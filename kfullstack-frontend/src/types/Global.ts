export type Project = {
  id: number;
  name: string;
  description: string;
};

export type LoginForm = {
  username: string;
  password: string;
};


export type Task = {
  id: number;
  title: string;
  description: string;
  status: "PENDING" | "IN_PROGRESS" | "DONE";
  dueDate: string;
  assignedTo?: {
    id: number;
    username: string;
  };
};

export type FormData = {
  name: string;
  description: string;
};

export type Props = {
  onSave: (name: string, description: string) => void;
  onClose: () => void;
  initialName?: string;
  initialDescription?: string;
};
