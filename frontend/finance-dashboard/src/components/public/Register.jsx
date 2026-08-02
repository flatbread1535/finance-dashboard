import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import ValidationMessage from "../../ValidationMessage";
import "../../styles/public/PublicLayout.css";
import "../../styles/public/Register.css";

const Register = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const navigate = useNavigate();

  const validateFormData = (formData) => {
    const errors = {};

    // Username validation
    if (!formData.username.trim()) {
      errors.username = "Username cannot be blank.";
    } else if (formData.username.length < 5) {
      errors.username = "Username must be at least 5 characters.";
    } else if (formData.username.length > 20) {
      errors.username = "Username cannot be more than 20 characters.";
    }

    // Password validation
    if (!formData.password) {
      errors.password = "Password cannot be blank.";
    } else if (formData.password.length < 8) {
      errors.password = "Password must be at least 8 characters.";
    } else if (formData.password.length > 20) {
      errors.password = "Password cannot be more than 20 characters.";
    }

    if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = "Passwords do not match.";
    }

    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (isSubmitting) return;

    const formData = {
      username: username.trim(),
      password,
      confirmPassword,
    };

    setErrors({});
    setSubmitError("");
    const validationErrors = validateFormData(formData);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await fetch(
        "http://localhost:8080/authentication/register",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: formData.username,
            password: formData.password,
          }),
        },
      );

      if (!response.ok) {
        let err = {};

        try {
          err = await response.json();
        } catch {
          console.log("No response");
        }

        if (err.errorMap && Object.keys(err.errorMap).length > 0) {
          setErrors(err.errorMap);
          return;
        }

        throw new Error(err.message || err.error || "Registration failed.");
      }

      const responseData = await response.json();
      localStorage.setItem("token", responseData.token);
      navigate("/dashboard");
    } catch (error) {
      setSubmitError(error.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="register-page">
      <form noValidate onSubmit={handleSubmit}>
        {/* TODO: Think of application name and maybe add slogan, information, or images */}
        {/* TODO: Add media query to split sign in form into two windows (form + graphic/animation)
         for larger windows */}
        <div className="registration-top">
          <h1>Sign up today</h1>
          <p>Create a free account</p>
        </div>

        <div className="input-container">
          {/* Username input field */}
          <div className="form-group">
            <label htmlFor="username">Your username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              aria-invalid={!!errors.username}
              required
            />
            <ValidationMessage message={errors.username} />
          </div>

          {/* Password input field */}
          <div className="form-group">
            <label htmlFor="password">Your Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="new-password"
              aria-invalid={!!errors.password}
              required
            />
            <ValidationMessage message={errors.password} />
          </div>

          {/* Password input field */}
          <div className="form-group">
            <label htmlFor="confirm-password">Confirm password</label>
            <input
              type="password"
              id="confirm-password"
              name="confirm-password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              autoComplete="new-password"
              aria-invalid={!!errors.confirmPassword}
              required
            />
            <ValidationMessage message={errors.confirmPassword} />
          </div>

          <div className="submission register">
            {/* Display error message for submission issues */}
            {submitError && (
              <div className="submit-error" role="alert">
                {submitError}
              </div>
            )}
            <button
              type="submit"
              className="submit-btn"
              disabled={isSubmitting}
              aria-busy={isSubmitting}
            >
              {isSubmitting ? "Creating Account..." : "Sign Up"}
            </button>
          </div>
        </div>

        <div className="registration-bottom">
          <p className="login-prompt">
            Already have an account? <Link to="/login">Sign in</Link>
          </p>
        </div>
      </form>
    </main>
  );
};

export default Register;
