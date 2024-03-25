import axios from "axios";
import { useEffect, useState } from "react";

function HouseBox() {
  const [house, setHouse] = useState(null);

  // Get house information
  useEffect(() => {
    axios
      .get("localhost:8080/ap/house")
      .then((res) => console.log(res))
      .catch((err) => console.log(err));
  }, []);

  return <div>HouseBox</div>;
}

export default HouseBox;
