"use client";

import React, { useCallback, useEffect, useState } from "react";
import api from "@/lib/api";
import { CaptchaResponse } from "@/types";
import { RefreshCw } from "lucide-react";

interface CaptchaBoxProps {
  onCaptchaLoaded: (captchaId: string) => void;
}

export default function CaptchaBox({ onCaptchaLoaded }: CaptchaBoxProps) {
  const [captcha, setCaptcha] = useState<CaptchaResponse | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchCaptcha = useCallback(async () => {
    setLoading(true);
    try {
      const res = await api.get<CaptchaResponse>("/api/captcha/generate");
      setCaptcha(res.data);
      onCaptchaLoaded(res.data.captchaId);
    } catch (err) {
      console.error("Failed to load captcha", err);
    } finally {
      setLoading(false);
    }
  }, [onCaptchaLoaded]);

  useEffect(() => {
    let isMounted = true;

    const loadInitialCaptcha = async () => {
      setLoading(true);
      try {
        const res = await api.get<CaptchaResponse>("/api/captcha/generate");
        if (isMounted) {
          setCaptcha(res.data);
          onCaptchaLoaded(res.data.captchaId);
        }
      } catch (err) {
        console.error("Failed to load captcha", err);
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    loadInitialCaptcha();

    return () => {
      isMounted = false;
    };
  }, [onCaptchaLoaded]);

  return (
    <div className="flex items-center gap-3 p-2 bg-slate-50 border rounded-lg">
      {loading || !captcha ? (
        <div className="h-10 w-44 bg-slate-200 animate-pulse rounded" />
      ) : (
        <img
          src={captcha.imageBase64}
          alt="Captcha"
          className="h-10 border rounded bg-white select-none"
        />
      )}
      <button
        type="button"
        onClick={fetchCaptcha}
        disabled={loading}
        className="p-2 text-slate-600 hover:text-blue-600 transition disabled:opacity-50"
        title="Refresh Captcha"
      >
        <RefreshCw size={18} className={loading ? "animate-spin" : ""} />
      </button>
    </div>
  );
}