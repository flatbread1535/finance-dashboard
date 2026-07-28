import { Outlet } from "react-router-dom";
import AuthNavbar from "../components/AuthNavbar";

const PublicLayout = () => {
  return (
    <div className="public-layout">
      <AuthNavbar />
      <Outlet />
    </div>
  );
};

export default PublicLayout;
