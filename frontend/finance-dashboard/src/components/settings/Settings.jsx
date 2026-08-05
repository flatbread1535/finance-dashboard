import { useNavigate } from "react-router";
import { GoBackIcon } from "../../assets/svgs/SettingsIcons";
import "../../styles/settings/Settings.css";

const Settings = () => {
  const navigate = useNavigate();

  const handleBackBtn = () => {
    navigate(-1);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <main className="settings">
      <section className="back-btn-container">
        <button type="button" onClick={handleBackBtn}>
          <GoBackIcon />
          Go Back
        </button>
      </section>
      <section className="logout-container">
        <div>
          <p>Do you wish to log out?</p>
          <button type="button" onClick={handleLogout}>
            Log Out
          </button>
        </div>
      </section>
    </main>
  );
};

export default Settings;
