import { useState } from "react";
import TabExplorer from "./TabExplorer";
import SHBox from "./SHBox";

function SH() {
  // State to track the selected SH type
  const [selectedSH, setSelectedSH] = useState<string>("SHS");

  return (
    <>
      <div>
        <TabExplorer />
      </div>
      <div>
        <SHBox />
      </div>
    </>
  );
}

export default SH;
