export interface ApiResponse {
  success: boolean;
  message: string;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  accessToken: string;
  tokenType: string;
  role: "CANDIDATE" | "RECRUITER" | "ADMIN";
}

export interface CaptchaResponse {
  captchaId: string;
  imageBase64: string;
}

export interface FormFieldConfig {
  fieldKey: string;
  label: string;
  fieldType: "text" | "email" | "tel" | "textarea" | "file";
  required: boolean;
  placeholder?: string;
}

export interface JobResponse {
  id: number;
  title: string;
  description: string;
  experience: string;
  skills: string[];
  applicationFormSchema: FormFieldConfig[];
  status: "ACTIVE" | "CLOSED" | "DELETED";
  recruiterId: number;
  recruiterEmail: string;
  createdAt: string;
}