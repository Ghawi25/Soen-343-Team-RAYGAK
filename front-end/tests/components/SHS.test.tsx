import { render, screen } from "@testing-library/react";
import SHH from "../../src/components/SH/SHH";

describe("Login component tests", () => {
  it("Renders correctly initial document", async () => {
    render(<SHH />);

    expect(screen.getByText("SHH")).not.toBeNull;
  });
});
