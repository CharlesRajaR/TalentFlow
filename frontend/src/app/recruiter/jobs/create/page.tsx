"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import api from "@/lib/api";
import { FormFieldConfig, JobResponse } from "@/types";
import { Plus, Trash2 } from "lucide-react";

export default function CreateJobPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [experience, setExperience] = useState("");
  const [skills, setSkills] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);

  // Default application form requirements
  const [formFields, setFormFields] = useState<FormFieldConfig[]>([
    { fieldKey: "fullName", label: "Full Name", fieldType: "text", required: true, placeholder: "Your legal name" },
    { fieldKey: "email", label: "Email Address", fieldType: "email", required: true, placeholder: "you@example.com" },
    { fieldKey: "mobile", label: "Contact Number", fieldType: "tel", required: true, placeholder: "10-digit number" },
    { fieldKey: "resume", label: "Resume (PDF)", fieldType: "file", required: true, placeholder: "Upload CV" },
  ]);

  const addCustomField = () => {
    const key = `field_${Date.now()}`;
    setFormFields((prev) => [
      ...prev,
      { fieldKey: key, label: "", fieldType: "text", required: false, placeholder: "" },
    ]);
  };

  const removeField = (index: number) => {
    setFormFields((prev) => prev.filter((_, i) => i !== index));
  };

  const updateField = (index: number, key: keyof FormFieldConfig, value: unknown) => {
    const updated = [...formFields];
    (updated[index] as unknown)[key] = value;
    setFormFields(updated);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      const payload = {
        title,
        experience,
        description,
        skills: skills.split(",").map((s) => s.trim()).filter(Boolean),
        formFields,
      };

      const res = await api.post<JobResponse>("/api/v1/jobs", payload);
      alert(`Job published! Share this link with candidates: /jobs/${res.data.id}`);
      router.push(`/jobs/${res.data.id}`);
    } catch (err: unknown) {
      alert(err.response?.data?.message || "Failed to create job posting");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto my-10 p-8 bg-white border border-slate-200 rounded-xl shadow-sm">
      <h1 className="text-2xl font-bold text-slate-900 mb-2">Publish a New Job Opening</h1>
      <p className="text-sm text-slate-500 mb-6">
        Specify job details and configure dynamic application fields for candidates.
      </p>

      <form onSubmit={handleSubmit} className="space-y-5">
        <div>
          <label className="text-xs font-semibold text-slate-700">Job Title</label>
          <input
            required
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full border rounded-lg p-2.5 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
            placeholder="e.g., Senior Full-Stack Engineer"
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="text-xs font-semibold text-slate-700">Required Experience</label>
            <input
              required
              value={experience}
              onChange={(e) => setExperience(e.target.value)}
              className="w-full border rounded-lg p-2.5 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="e.g., 3-5 years"
            />
          </div>
          <div>
            <label className="text-xs font-semibold text-slate-700">Skills (Comma-separated)</label>
            <input
              required
              value={skills}
              onChange={(e) => setSkills(e.target.value)}
              className="w-full border rounded-lg p-2.5 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
              placeholder="Java, Spring Boot, React, MySQL"
            />
          </div>
        </div>

        <div>
          <label className="text-xs font-semibold text-slate-700">Job Description</label>
          <textarea
            required
            rows={4}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full border rounded-lg p-2.5 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
            placeholder="Describe role responsibilities, team culture, and qualifications..."
          />
        </div>

        {/* Dynamic Schema Builder */}
        <div className="pt-5 border-t">
          <div className="flex justify-between items-center mb-3">
            <div>
              <h3 className="text-sm font-bold text-slate-800">Candidate Application Form Fields</h3>
              <p className="text-xs text-slate-500">Customize the exact inputs applicants must submit</p>
            </div>
            <button
              type="button"
              onClick={addCustomField}
              className="flex items-center gap-1.5 text-xs bg-blue-50 text-blue-600 font-semibold px-3 py-1.5 rounded-lg hover:bg-blue-100 transition"
            >
              <Plus size={15} /> Add Custom Field
            </button>
          </div>

          <div className="space-y-3">
            {formFields.map((field, idx) => (
              <div key={idx} className="flex gap-2 items-center bg-slate-50 border p-2.5 rounded-lg">
                <input
                  required
                  placeholder="Field Label (e.g. GitHub URL)"
                  value={field.label}
                  onChange={(e) => updateField(idx, "label", e.target.value)}
                  className="border rounded-md px-2.5 py-1.5 text-xs flex-1 outline-none bg-white"
                />
                <select
                  value={field.fieldType}
                  onChange={(e) => updateField(idx, "fieldType", e.target.value)}
                  className="border rounded-md px-2 py-1.5 text-xs bg-white outline-none"
                >
                  <option value="text">Single Line Text</option>
                  <option value="email">Email</option>
                  <option value="tel">Phone</option>
                  <option value="textarea">Multi-line Text</option>
                  <option value="file">File Upload (PDF)</option>
                </select>
                <label className="flex items-center gap-1.5 text-xs text-slate-600 select-none cursor-pointer">
                  <input
                    type="checkbox"
                    checked={field.required}
                    onChange={(e) => updateField(idx, "required", e.target.checked)}
                  />
                  Required
                </label>
                <button
                  type="button"
                  onClick={() => removeField(idx)}
                  className="p-1.5 text-slate-400 hover:text-red-500 transition"
                >
                  <Trash2 size={16} />
                </button>
              </div>
            ))}
          </div>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg transition disabled:opacity-50 mt-4"
        >
          {loading ? "Publishing Job..." : "Publish Job Opening"}
        </button>
      </form>
    </div>
  );
}