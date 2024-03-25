import { render, screen } from "@testing-library/react";
import SHC from "../../src/components/SH/SHC";

describe("Login component tests", () => {
  it("Renders correctly initial document", async () => {
    render(<SHC />);

    expect(screen.getByText("SHC")).not.toBeNull;
  });
});
