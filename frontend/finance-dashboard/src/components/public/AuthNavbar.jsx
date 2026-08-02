import { useLocation, Link } from "react-router-dom";
import "../../styles/public/AuthNavbar.css";

const AuthNavbar = () => {
  const location = useLocation();
  const isHomePage = location.pathname === "/";

  return (
    <nav className="auth-navbar">
      <Link to="/" className="home-link">Campus Spend</Link>

      {isHomePage && (
        <div className="auth-btns">
          <Link to="/register" className="sign-up-btn">Sign Up</Link>
          <Link to="/login" className="login-btn">Login</Link>
        </div>
      )}
    </nav>
  );
};

export default AuthNavbar;
