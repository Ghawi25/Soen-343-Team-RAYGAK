function TabExplorer({
  setSelectedSH,
}: {
  setSelectedSH: (sh: string) => void;
}) {
  return (
    <div className="feature-nav">
      <button onClick={() => setSelectedSH("SHS")}>SHS</button>
      <button onClick={() => setSelectedSH("SHC")}>SHC</button>
      <button onClick={() => setSelectedSH("SHP")}>SHP</button>
      <button onClick={() => setSelectedSH("SHH")}>SHH</button>
      <button>+</button>
    </div>
  );
}

export default TabExplorer;
