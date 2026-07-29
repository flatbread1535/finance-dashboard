import { Outlet } from "react-router-dom";
import DashboardNavbar from "../components/DashboardNavbar";
import DashboardSidebar from "../components/DashboardSidebar";

const DashboardLayout = () => {
  return (
    <div className="dashboard-layout">
      <DashboardNavbar />
      <DashboardSidebar />
      <Outlet />
    </div>
  );
};

export default DashboardLayout;
