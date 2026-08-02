import { useLocation } from "react-router-dom";
import { ToggleSidebarIcon } from "../../assets/svgs/NavbarIcons";
import "../../styles/dashboard-layout/DashboardLayout.css";
import "../../styles/dashboard-layout/DashboardNavbar.css";

const DashboardNavbar = ({ setIsSidebarCollapsed }) => {
  const location = useLocation();

  const pageNames = {
    "/dashboard": "Dashboard",
    "/transactions": "Transactions",
    "/budgets": "Budgets",
    "/goals": "Goals",
    "/groups": "Groups",
  };

  const pageName = pageNames[location.pathname] ?? "Dashboard";

  return (
    <nav className="dashboard-navbar">
      <section className="sidebar-navbar-section">
        <button
          type="button"
          className="toggle-sidebar-btn"
          aria-label="Toggle sidebar"
          onClick={() => setIsSidebarCollapsed((prev) => !prev)}
        >
          <ToggleSidebarIcon />
        </button>
      </section>
      <section className="main-navbar-section">
        <h1>{pageName}</h1>
      </section>
      <section className="logo-navbar-section">
        <h2>Campus Spend</h2>
      </section>
    </nav>
  );
};

export default DashboardNavbar;
