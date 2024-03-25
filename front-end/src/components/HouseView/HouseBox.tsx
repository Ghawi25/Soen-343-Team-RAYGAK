import axios from "axios";
import { useEffect, useState } from "react";

function HouseBox() {
  const [house, setHouse] = useState(null);

  // Get house information
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((res) => setHouse(res.data))
      .catch((err) => console.log(err));
  }, []);

  if(!house) {
    return <div>Loading...</div>
  } else {
    console.log(house);
    
    return <div>HouseBox</div>
  }
}

export default HouseBox;
