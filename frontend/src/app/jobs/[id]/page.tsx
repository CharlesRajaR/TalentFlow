"use client";

import React, { useEffect, useState, use } from "react";
import api from "@/lib/api";
import { JobResponse } from "@/types";

export default function PublicJobApplyPage({ params }: { params: Promise<{ id: string }> }) {
  const resolvedParams = use(params);
  const jobId = resolvedParams.id;

  const [job, setJob] = useState<JobResponse | null>(null);
  const [formData, setFormData] = useState<Record<string, string>>({});
  const [files, setFiles] = useState<Record<string, File>>({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get<JobResponse>(`/api/v1/jobs/${jobId}`)
      .then((res) => setJob(res.data))
      .catch((err) => console.error("Failed to load job", err))
      .finally(() => setLoading(false));
  }, [jobId]);

  const handleTextChange = (key: string, value: string) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
  };

  const handleFileChange = (key: string, file: File | undefined) => {
    if (file) {
      setFiles((prev) => ({ ...prev, [key]: file }));
    }
  };

  const handleApply = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Candidate Form Responses:", formData);
    console.log("Candidate Attached Files:", files);
    alert("Application data captured! Ready for the submission endpoint.");
  };

  if (loading) return <div className="p-12 text-center text-slate-500">Loading opening...</div>;
  if (!job) return <div className="p-12 text-center text-red-500">Job not found</div>;

  return (
    <div className="max-w-2xl mx-auto my-10 p-8 bg-white border border-slate-200 rounded-xl shadow-sm">
      {/* Job Info Header */}
      <div className="border-b pb-6 mb-6">
        <h1 className="text-3xl font-extrabold text-slate-900">{job.title}</h1>
        <p className="text-sm font-medium text-blue-600 mt-1">Experience: {job.experience}</p>
        
        <div className="flex flex-wrap gap-2 mt-4">
          {job.skills.map((skill, idx) => (
            <span key={idx} className="bg-slate-100 text-slate-700 text-xs px-2.5 py-1 rounded-md font-medium">
              {skill}
            </span>
          ))}
        </div>

        <p className="mt-5 text-sm text-slate-600 whitespace-pre-line leading-relaxed">{job.description}</p>
      </div>

      {/* Dynamic Application Form */}
      <h2 className="text-xl font-bold text-slate-900 mb-4">Apply for this Position</h2>
      <form onSubmit={handleApply} className="space-y-4">
        {job.applicationFormSchema.map((field) => (
          <div key={field.fieldKey}>
            <label className="text-xs font-semibold text-slate-700 block mb-1">
              {field.label} {field.required && <span className="text-red-500">*</span>}
            </label>

            {field.fieldType === "file" ? (
              <input
                type="file"
                required={field.required}
                onChange={(e) => handleFileChange(field.fieldKey, e.target.files?.[0])}
                className="w-full text-xs text-slate-500 file:mr-3 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-xs file:font-semibold file:bg-blue-50 file:text-blue-600 hover:file:bg-blue-100"
              />
            ) : field.fieldType === "textarea" ? (
              <textarea
                required={field.required}
                placeholder={field.placeholder}
                rows={3}
                onChange={(e) => handleTextChange(field.fieldKey, e.target.value)}
                className="w-full border rounded-lg p-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
              />
            ) : (
              <input
                type={field.fieldType}
                required={field.required}
                placeholder={field.placeholder}
                onChange={(e) => handleTextChange(field.fieldKey, e.target.value)}
                className="w-full border rounded-lg p-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
              />
            )}
          </div>
        ))}

        <button
          type="submit"
          className="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-semibold py-3 rounded-lg transition mt-6"
        >
          Submit Application
        </button>
      </form>
    </div>
  );
}