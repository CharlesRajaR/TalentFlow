"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import api from "@/lib/api";
import CaptchaBox from "@/components/CaptchaBox";

export default function CandidateRegisterPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobile: "",
    dob: "",
    password: "",
    captchaInput: "",
  });
  const [captchaId, setCaptchaId] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await api.post("/api/v1/auth/register", {
        ...formData,
        captchaId,
      });
      alert("Registration successful! Please login.");
      router.push("/candidate/login");
    } catch (err: unknown) {
      setError(err.response?.data?.message || "Registration failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 p-4">
      <div className="max-w-md w-full bg-white p-8 rounded-xl shadow-sm border border-slate-200">
        <h2 className="text-2xl font-bold text-slate-900 mb-1">Create Candidate Account</h2>
        <p className="text-sm text-slate-500 mb-6">Start applying for top roles</p>

        {error && (
          <div className="mb-4 p-3 bg-red-50 text-red-600 text-sm rounded-lg border border-red-200">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-xs font-medium text-slate-700">First Name</label>
              <input
                required
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
              />
            </div>
            <div>
              <label className="text-xs font-medium text-slate-700">Last Name</label>
              <input
                required
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-slate-700">Email Address</label>
            <input
              required
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="text-xs font-medium text-slate-700">Mobile Number</label>
              <input
                required
                name="mobile"
                value={formData.mobile}
                onChange={handleChange}
                className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
              />
            </div>
            <div>
              <label className="text-xs font-medium text-slate-700">Date of Birth</label>
              <input
                required
                type="date"
                name="dob"
                value={formData.dob}
                onChange={handleChange}
                className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
              />
            </div>
          </div>

          <div>
            <label className="text-xs font-medium text-slate-700">Password</label>
            <input
              required
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          <div className="space-y-2 pt-2">
            <label className="text-xs font-medium text-slate-700">Verify CAPTCHA</label>
            <CaptchaBox onCaptchaLoaded={setCaptchaId} />
            <input
              required
              name="captchaInput"
              placeholder="Enter text shown above"
              value={formData.captchaInput}
              onChange={handleChange}
              className="w-full border rounded-lg p-2 text-sm uppercase tracking-widest focus:ring-2 focus:ring-blue-500 outline-none"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2.5 rounded-lg transition disabled:opacity-50 mt-2"
          >
            {loading ? "Registering..." : "Create Account"}
          </button>
        </form>

        <p className="text-xs text-center text-slate-500 mt-5">
          Already have an account?{" "}
          <Link href="/candidate/login" className="text-blue-600 hover:underline">
            Login here
          </Link>
        </p>
      </div>
    </div>
  );
}