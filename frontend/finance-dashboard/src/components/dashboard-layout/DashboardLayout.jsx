import { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import DashboardNavbar from "./DashboardNavbar";
import DashboardSidebar from "./DashboardSidebar";
import "../../styles/dashboard-layout/DashboardLayout.css";

const DashboardLayout = () => {
  const [account, setAccount] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);

  useEffect(() => {
    const getAccountInfo = async () => {
      try {
        const token = localStorage.getItem("token");

        if (!token) {
          window.location.href = "/login";
          return;
        }

        const response = await fetch("http://localhost:8080/accounts/me", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 401) {
          localStorage.removeItem("token");
          window.location.href = "/login";
          return;
        }

        if (!response.ok) {
          throw new Error("Failed to load account.");
        }

        const accountData = await response.json();
        setAccount(accountData);
      } catch (error) {
        setError(error.message);
      } finally {
        setIsLoading(false);
      }
    };

    getAccountInfo();
  }, []);

  if (isLoading) {
    return <div>Loading dashboard...</div>;
  }

  if (error) {
    return (
      <div className="dashboard-error">
        <h2>Unable to load dashboard...</h2>
        <p>{error}</p>
      </div>
    );
  }

  return (
    <div
      className={`dashboard-layout 
        ${isSidebarCollapsed ? "sidebar-collapsed" : ""}`}
    >
      <DashboardNavbar
        isSidebarCollapsed={isSidebarCollapsed}
        setIsSidebarCollapsed={setIsSidebarCollapsed}
      />
      <DashboardSidebar
        account={account}
        isSidebarCollapsed={isSidebarCollapsed}
      />
      <Outlet context={{ account }} />
    </div>
  );
};

export default DashboardLayout;
