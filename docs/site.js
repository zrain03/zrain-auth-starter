"use strict";
(() => {
  const buttons = [...document.querySelectorAll(".tab-button")];
  const method = document.getElementById("request-method");
  const path = document.getElementById("request-path");
  const fields = document.getElementById("demo-fields");
  const emailInput = document.getElementById("demo-email");
  const passwordInput = document.getElementById("demo-password");
  const fieldNote = document.getElementById("demo-field-note");
  const responseBody = document.getElementById("response-body");
  const status = document.getElementById("http-status");
  const hint = document.getElementById("response-hint");
  const sessionDot = document.getElementById("session-dot");
  const sessionLabel = document.getElementById("session-label");
  const execute = document.getElementById("execute-demo");
  const reset = document.getElementById("reset-demo");
  const copy = document.getElementById("copy-response");

  const endpoints = {
    register: { method: "POST", path: "/api/v1/auth/register" },
    login: { method: "POST", path: "/api/v1/auth/login" },
    me: { method: "GET", path: "/api/v1/users/me" },
    admin: { method: "GET", path: "/api/v1/demo/admin" }
  };

  let mode = "register";
  let demoUser = null;
  let authenticated = false;
  let lastJson = "";

  function setResponse(code, payload, message) {
    lastJson = JSON.stringify(payload, null, 2);
    responseBody.textContent = lastJson;
    status.textContent = code;
    status.className = "status-pill " + (code.startsWith("2") ? "success" : code === "READY" ? "neutral" : "client-error");
    hint.textContent = message;
  }

  function updateSession() {
    sessionDot.classList.toggle("online", authenticated);
    sessionLabel.textContent = authenticated ? "Demo session active · USER role" : demoUser ? "Demo user registered · not signed in" : "No demo session";
  }

  function setMode(next) {
    mode = next;
    buttons.forEach(button => {
      const active = button.dataset.mode === mode;
      button.classList.toggle("active", active);
      button.setAttribute("aria-selected", String(active));
      button.tabIndex = active ? 0 : -1;
      if (active) fields.setAttribute("aria-labelledby", button.id);
    });
    method.textContent = endpoints[mode].method;
    path.textContent = endpoints[mode].path;
    fields.hidden = mode === "me" || mode === "admin";
    fieldNote.textContent = "Fictional values only · not sent to any server.";
    setResponse("READY", { endpoint: endpoints[mode].path, instruction: "Run the browser-only simulation." }, "No network requests will be made.");
  }

  function run() {
    const email = emailInput.value.trim().toLowerCase();
    const password = passwordInput.value;
    const invalid = !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email) || email.length > 254 || password.length < 12 || password.length > 72;

    switch (mode) {
      case "register":
        if (invalid) {
          setResponse("400", { status: 400, code: "VALIDATION_ERROR", message: "Use a sample email and a demo password of 12–72 characters." }, "Simulated input validation.");
          return;
        }
        if (demoUser && demoUser.email === email) {
          setResponse("409", { status: 409, code: "EMAIL_ALREADY_REGISTERED", message: "This demo user already exists." }, "Duplicate email is rejected.");
          return;
        }
        demoUser = { email };
        authenticated = false;
        updateSession();
        setResponse("201", { id: "demo-user-0001", email, role: "USER" }, "Simulated registration. No password stored.");
        return;
      case "login":
        if (!demoUser || demoUser.email !== email || password !== "DemoPassword123!") {
          setResponse("401", { status: 401, code: "INVALID_CREDENTIALS", message: "Incorrect demo email or demo password." }, "Register with the default sample password, then login.");
          return;
        }
        authenticated = true;
        updateSession();
        setResponse("200", { accessToken: "SIMULATED.NOT_A_REAL_JWT.NOT_SIGNED", tokenType: "Bearer", expiresIn: 900 }, "Illustrative placeholder only — not signed, valid, or usable on an API.");
        return;
      case "me":
        if (!authenticated) {
          setResponse("401", { status: 401, code: "UNAUTHORIZED", message: "Authentication required." }, "Login in the demo first.");
          return;
        }
        setResponse("200", { id: "demo-user-0001", email: demoUser.email, role: "USER" }, "The simulated user has permission to view their profile.");
        return;
      case "admin":
        if (!authenticated) {
          setResponse("401", { status: 401, code: "UNAUTHORIZED", message: "Authentication required." }, "An authenticated session is required.");
          return;
        }
        setResponse("403", { status: 403, code: "FORBIDDEN", message: "Insufficient permissions." }, "The simulated USER role cannot access ADMIN.");
        return;
    }
  }

  buttons.forEach((button, index) => {
    button.addEventListener("click", () => setMode(button.dataset.mode));
    button.addEventListener("keydown", event => {
      if (event.key !== "ArrowRight" && event.key !== "ArrowLeft") return;
      event.preventDefault();
      const next = buttons[(index + (event.key === "ArrowRight" ? 1 : -1) + buttons.length) % buttons.length];
      next.focus();
      setMode(next.dataset.mode);
    });
  });
  execute.addEventListener("click", run);
  reset.addEventListener("click", () => {
    demoUser = null;
    authenticated = false;
    emailInput.value = "demo@example.com";
    passwordInput.value = "DemoPassword123!";
    updateSession();
    setMode("register");
  });
  copy.addEventListener("click", async () => {
    try {
      await navigator.clipboard.writeText(lastJson);
      copy.textContent = "Copied!";
    } catch {
      copy.textContent = "Copy unavailable";
    }
    copy.disabled = true;
    window.setTimeout(() => {
      copy.textContent = "Copy JSON";
      copy.disabled = false;
    }, 1500);
  });
  setMode("register");
  updateSession();
})();
