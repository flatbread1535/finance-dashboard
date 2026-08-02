import {
  DashboardIcon,
  TransactionsIcon,
  BudgetsIcon,
  GoalsIcon,
  GroupsIcon,
  SettingsIcon,
} from "../../assets/svgs/SidebarIcons";
import { Link } from "react-router-dom";
import "../../styles/dashboard-layout/DashboardLayout.css";
import "../../styles/dashboard-layout/DashboardSidebar.css";

const DashboardSidebar = ({ account, isSidebarCollapsed }) => {
  const profileUrl = account.profilePictureUrl;
  const username = account.username;
  const defaultProfileUrl = "../assets/images/default-profile-img.jpg";

  return (
    <aside
      className={`dashboard-sidebar 
    ${isSidebarCollapsed ? "collapsed" : ""}`}
    >
      <section className="features">
        <Link to="/dashboard">
          <DashboardIcon />
          Dashboard
        </Link>
        <Link to="/transactions">
          <TransactionsIcon />
          Transactions
        </Link>
        <Link to="/budgets">
          <BudgetsIcon />
          Budgets
        </Link>
        <Link to="/goals">
          <GoalsIcon />
          Goals
        </Link>
        <Link to="/groups">
          <GroupsIcon />
          Groups
        </Link>
      </section>
      <section className="profile">
        <div className="profile-information">
          {profileUrl ? (
            <img
              src={profileUrl}
              alt={`${username}'s profile picture`}
              className="profile-picture"
            ></img>
          ) : (
            <img
              src={defaultProfileUrl}
              alt="Default profile picture"
              className="profile-picture"
            ></img>
          )}
          <p className="username">{username}</p>
        </div>
        <Link to="/settings" aria-label="settings">
          <SettingsIcon />
        </Link>
      </section>
    </aside>
  );
};

export default DashboardSidebar;
