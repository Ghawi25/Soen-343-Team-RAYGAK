import axios from "axios";
import { useEffect, useState } from "react";

// function to generate a rectangle for the room
const Room = ({ x, y, width, height }) => {
  return <rect x={x} y={y} width={width*100} height={height*100} fill="black"/>
}

// Function to render all rooms
const renderRooms = (rooms) => {
  const visitedRooms = [];
  return rooms.map((room) => {
    if(room in visitedRooms) return;
    visitedRooms.push(room);
    const adjacentRooms = []
    adjacentRooms.push({top: room.topAdjacentRoom, bottom: room.bottomAdjacentRoom, left: room.leftAdjacentRoom, right: room.rightAdjacentRoom})
    console.log(adjacentRooms)
    return <Room x={0} y={0} width={room.width} height={room.height}/>
  })
}

function HouseBox() {
  const [house, setHouse] = useState(null);

  // Get house information
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/house")
      .then((res) => setHouse(res.data))
      .catch((err) => console.log(err));
  }, []);

  if (!house) {
    return <div>Loading...</div>
  } else {
    const rooms = house.rooms;
    return (
      <svg width="100%" height="100%">
        {renderRooms(rooms)}
      </svg>
    )
  }
}

export default HouseBox;
