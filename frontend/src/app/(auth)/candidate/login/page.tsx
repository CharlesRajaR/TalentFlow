"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import api from "@/lib/api";
import { AuthResponse } from "@/types";

type LoginTab = "EMAIL_PASSWORD" | "EMAIL_OTP" | "MOBILE_OTP";

export default function CandidateLoginPage() {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState<LoginTab>("EMAIL_PASSWORD");
  
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [mobile, setMobile] = useState("");
  const [otp, setOtp] = useState("");
  
  const [timer, setTimer] = useState(0);
  const [otpSent, setOtpSent] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let interval: NodeJS.Timeout;
    if (timer > 0) {
      interval = setInterval(() => setTimer((t) => t - 1), 1000);
    }
    return () => clearInterval(interval);
  }, [timer]);

  const handleSendOtp = async (channel: "EMAIL" | "MOBILE") => {
    setError("");
    try {
      await api.post("/api/v1/auth/send-otp", {
        channel,
        email: channel === "EMAIL" ? email : undefined,
        mobile: channel === "MOBILE" ? mobile : undefined,
      });
      setOtpSent(true);
      setTimer(60); // 60-second cooldown
      alert(`OTP dispatched! (Check backend terminal in development)`);
    } catch (err: unknown) {
      setError(err.response?.data?.message || "Failed to send OTP");
    }
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const payload: unknown = { loginType: activeTab };
      if (activeTab === "EMAIL_PASSWORD") {
        payload.email = email;
        payload.password = password;
      } else if (activeTab === "EMAIL_OTP") {
        payload.email = email;
        payload.otp = otp;
      } else if (activeTab === "MOBILE_OTP") {
        payload.mobile = mobile;
        payload.otp = otp;
      }

      const res = await api.post<AuthResponse>("/api/v1/auth/login", payload);
      localStorage.setItem("token", res.data.accessToken);
      localStorage.setItem("role", res.data.role);

      router.push("/");
    } catch (err: unknown) {
      setError(err.response?.data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 p-4">
      <div className="max-w-md w-full bg-white p-8 rounded-xl shadow-sm border border-slate-200">
        <h2 className="text-2xl font-bold text-slate-900 mb-1">Candidate Login</h2>
        <p className="text-sm text-slate-500 mb-6">Choose your preferred login method</p>

        {/* Tab Navigation */}
        <div className="flex border-b mb-6 text-xs font-semibold">
          <button
            onClick={() => { setActiveTab("EMAIL_PASSWORD"); setError(""); }}
            className={`pb-2 flex-1 text-center transition ${
              activeTab === "EMAIL_PASSWORD" ? "border-b-2 border-blue-600 text-blue-600" : "text-slate-500"
            }`}
          >
            Password
          </button>
          <button
            onClick={() => { setActiveTab("EMAIL_OTP"); setError(""); }}
            className={`pb-2 flex-1 text-center transition ${
              activeTab === "EMAIL_OTP" ? "border-b-2 border-blue-600 text-blue-600" : "text-slate-500"
            }`}
          >
            Email OTP
          </button>
          <button
            onClick={() => { setActiveTab("MOBILE_OTP"); setError(""); }}
            className={`pb-2 flex-1 text-center transition ${
              activeTab === "MOBILE_OTP" ? "border-b-2 border-blue-600 text-blue-600" : "text-slate-500"
            }`}
          >
            Mobile OTP
          </button>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-red-50 text-red-600 text-sm rounded-lg border border-red-200">
            {error}
          </div>
        )}

        <form onSubmit={handleLogin} className="space-y-4">
          {/* Tab 1: Email + Password */}
          {activeTab === "EMAIL_PASSWORD" && (
            <>
              <div>
                <label className="text-xs font-medium text-slate-700">Email Address</label>
                <input
                  required
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
                />
              </div>
              <div>
                <label className="text-xs font-medium text-slate-700">Password</label>
                <input
                  required
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full border rounded-lg p-2 text-sm mt-1 focus:ring-2 focus:ring-blue-500 outline-none"
                />
              </div>
            </>
          )}

          {/* Tab 2: Email + OTP */}
          {activeTab === "EMAIL_OTP" && (
            <>
              <div>
                <label className="text-xs font-medium text-slate-700">Email Address</label>
                <div className="flex gap-2 mt-1">
                  <input
                    required
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="w-full border rounded-lg p-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                  <button
                    type="button"
                    disabled={!email || timer > 0}
                    onClick={() => handleSendOtp("EMAIL")}
                    className="whitespace-nowrap px-3 py-1 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-medium rounded-lg disabled:opacity-50"
                  >
                    {timer > 0 ? `${timer}s` : "Get OTP"}
                  </button>
                </div>
              </div>
              {otpSent && (
                <div>
                  <label className="text-xs font-medium text-slate-700">Enter 6-Digit OTP</label>
                  <input
                    required
                    maxLength={6}
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    className="w-full border rounded-lg p-2 text-sm mt-1 tracking-widest text-center font-mono focus:ring-2 focus:ring-blue-500 outline-none"
                    placeholder="000000"
                  />
                </div>
              )}
            </>
          )}

          {/* Tab 3: Mobile + OTP */}
          {activeTab === "MOBILE_OTP" && (
            <>
              <div>
                <label className="text-xs font-medium text-slate-700">Mobile Number</label>
                <div className="flex gap-2 mt-1">
                  <input
                    required
                    type="tel"
                    value={mobile}
                    onChange={(e) => setMobile(e.target.value)}
                    className="w-full border rounded-lg p-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                  />
                  <button
                    type="button"
                    disabled={!mobile || timer > 0}
                    onClick={() => handleSendOtp("MOBILE")}
                    className="whitespace-nowrap px-3 py-1 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-medium rounded-lg disabled:opacity-50"
                  >
                    {timer > 0 ? `${timer}s` : "Get OTP"}
                  </button>
                </div>
              </div>
              {otpSent && (
                <div>
                  <label className="text-xs font-medium text-slate-700">Enter 6-Digit OTP</label>
                  <input
                    required
                    maxLength={6}
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    className="w-full border rounded-lg p-2 text-sm mt-1 tracking-widest text-center font-mono focus:ring-2 focus:ring-blue-500 outline-none"
                    placeholder="000000"
                  />
                </div>
              )}
            </>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2.5 rounded-lg transition disabled:opacity-50 mt-2"
          >
            {loading ? "Verifying..." : "Sign In"}
          </button>
        </form>

        <p className="text-xs text-center text-slate-500 mt-5">
          New candidate?{" "}
          <Link href="/candidate/register" className="text-blue-600 hover:underline">
            Register here
          </Link>
        </p>
      </div>
    </div>
  );
}