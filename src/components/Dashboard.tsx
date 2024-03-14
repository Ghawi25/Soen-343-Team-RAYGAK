import HouseView from "./HouseView";
import Simulation from "./Simulation";
import SH from "./SH";
import OutputConsole from "./OutputConsole";

function Dashboard() {
  return (
    <>
      <section id="simulation" className="module">
        <Simulation />
      </section>
      <section id="SH" className="module">
        <SH />
      </section>
      <section id="house-view" className="module">
        <HouseView />
      </section>
      <section id="output-console" className="module">
        <OutputConsole />
      </section>
    </>
  );
}

export default Dashboard;