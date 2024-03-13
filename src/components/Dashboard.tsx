import HouseView from "./HouseView";
import Simulation from "./Simulation";
import SH from "./SH";
import OutputConsole from "./OutputConsole";

function Dashboard() {
  return (
    <>
      <div className="module">
        <Simulation />
      </div>
      <div className="module">
        <SH />
      </div>
      <div className="module">
        <HouseView />
      </div>
      <div className="module">
        <OutputConsole />
      </div>
    </>
  );
}

export default Dashboard;
