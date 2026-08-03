import demoImage from "../../assets/images/web-demo.png";
import "../../styles/public/PublicLayout.css";
import "../../styles/public/HomePage.css";

const HomePage = () => {
  return (
    <main className="home-page">
      <section className="info">
        <p>A personal finance dashboard to manage college expenses</p>
        <ul>
          <li>Gives important sumaries</li>
          <li>Displays interactive charts</li>
          <li>Tracks your transactions</li>
        </ul>
      </section>
      <section className="demo-screenshot-container">
        <img
          src={demoImage}
          className="demo-screenshot"
          alt="Website demo image with view of transaction page table chart"
        ></img>
      </section>
    </main>
  );
};

export default HomePage;
