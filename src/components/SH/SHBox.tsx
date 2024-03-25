import SHC from "./SHC";
import SHS from "./SHS";
import SHP from "./SHP";
import SHH from "./SHH";

function SHBox({ selectedSH }: { selectedSH: string }) {
  const renderSH = () => {
    switch (selectedSH) {
      case "SHS":
        return <SHS />;
      case "SHC":
        return <SHC />;
      case "SHP":
        return <SHP />;
      case "SHH":
        return <SHH />;
      default:
        return <SHS />; // Default or fallback component
    }
  };

  return <>{renderSH()}</>;
}

export default SHBox;
