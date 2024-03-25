import { render, screen } from "@testing-library/react";
import SHP from "../../src/components/SH/SHP";

describe("Login component tests", () => {
  it("Renders correctly initial document", async () => {
    render(<SHP />);

    expect(screen.getByText("SHP")).not.toBeNull;
  });
});
