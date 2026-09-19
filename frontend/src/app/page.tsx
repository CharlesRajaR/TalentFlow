import Link from "next/link";
import { Briefcase, UserCheck, PlusCircle, LogIn } from "lucide-react";

export default function HomePage() {
  return (
    <main className="min-h-screen bg-slate-50 flex flex-col justify-center items-center px-4 py-12">
      <div className="max-w-3xl w-full text-center mb-10">
        <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight sm:text-5xl">
          Welcome to <span className="text-blue-600">TalentFlow</span>
        </h1>
        <p className="mt-4 text-lg text-slate-600">
          Smart recruitment core engine. Streamlining candidate applications and recruiter workflows.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-3xl w-full">
        {/* Candidate Card */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex flex-col justify-between">
          <div>
            <div className="w-12 h-12 bg-blue-100 text-blue-600 rounded-xl flex items-center justify-center mb-4">
              <UserCheck size={24} />
            </div>
            <h2 className="text-xl font-bold text-slate-900 mb-2">Candidates</h2>
            <p className="text-sm text-slate-500 mb-6">
              Browse openings, complete custom application forms, and track your interview pipeline.
            </p>
          </div>

          <div className="space-y-3">
            <Link
              href="/candidate/login"
              className="w-full flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-medium py-2.5 rounded-lg transition text-sm"
            >
              <LogIn size={16} /> Candidate Login
            </Link>
            <Link
              href="/candidate/register"
              className="w-full flex items-center justify-center gap-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium py-2.5 rounded-lg transition text-sm"
            >
              Register Account
            </Link>
          </div>
        </div>

        {/* Recruiter Card */}
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex flex-col justify-between">
          <div>
            <div className="w-12 h-12 bg-emerald-100 text-emerald-600 rounded-xl flex items-center justify-center mb-4">
              <Briefcase size={24} />
            </div>
            <h2 className="text-xl font-bold text-slate-900 mb-2">Recruiters</h2>
            <p className="text-sm text-slate-500 mb-6">
              Post job requisitions, customize required dynamic applicant fields, and review candidate profiles.
            </p>
          </div>

          <div className="space-y-3">
            <Link
              href="/recruiter/jobs/create"
              className="w-full flex items-center justify-center gap-2 bg-emerald-600 hover:bg-emerald-700 text-white font-medium py-2.5 rounded-lg transition text-sm"
            >
              <PlusCircle size={16} /> Create Job Post
            </Link>
            <Link
              href="/candidate/login"
              className="w-full flex items-center justify-center gap-2 bg-slate-100 hover:bg-slate-200 text-slate-700 font-medium py-2.5 rounded-lg transition text-sm"
            >
              Recruiter Portal
            </Link>
          </div>
        </div>
      </div>
    </main>
  );
}