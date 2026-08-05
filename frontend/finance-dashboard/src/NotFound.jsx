import { Link } from "react-router";

const NotFound = () => {
  return (
    <>
      <h1>404</h1>
      <p>Cannot find page.</p>
      <Link to="/">Return Home</Link>
    </>
  );
};

export default NotFound;
