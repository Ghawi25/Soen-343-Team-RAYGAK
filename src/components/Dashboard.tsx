import HouseView from "./HouseView";
import Simulation from "./Simulation";
import SH from "./SH";
import OutputConsole from "./OutputConsole";

function Dashboard() {
    return (<>
    <div><Simulation/></div>
    <div><SH/></div>
    <div><HouseView/></div>
    <div><OutputConsole/></div>
    </>);
}

export default Dashboard;