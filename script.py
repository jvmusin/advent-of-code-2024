#!/usr/bin/env python3

def process_circuit_file(filename):
    """Process a circuit definition file and return Graphviz DOT notation."""
    # Read and process the input file
    edges = []
    colors = []
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if not line:
                continue

            # Parse the line format: input1 OP input2 -> output
            parts = line.split('->')
            if len(parts) != 2:
                continue

            left_side = parts[0].strip()
            output = parts[1].strip()

            # Split the left side into inputs and operation
            inputs = left_side.split()
            if len(inputs) != 3:  # Should be: input1 OP input2
                continue

            input1, op, input2 = inputs

            # Create the intermediate node and edges
            edges.append((input1, f"{output}"))  # Edge from input1 to intermediate
            edges.append((input2, f"{output}"))  # Edge from input2 to intermediate
#             edges.append((f"{output}", output))  # Edge from intermediate to output
            color = ""
            if op == "AND":
                color = "red"
            if op == "OR":
                color = "green"
            if op == "XOR":
                color = "blue"
            colors.append((output, color))
            colors.append((op, color))

    # Generate the DOT notation
    dot_lines = ['digraph LogicCircuit {']
    dot_lines.append('    // Graph attributes')
    dot_lines.append('    rankdir=LR;')
    dot_lines.append('    node [shape=box];')
    dot_lines.append('')
    dot_lines.append('    // Special nodes for operations')
    dot_lines.append('    node [shape=ellipse];')
    dot_lines.append('')

    # Add all edges
    dot_lines.append('    // Circuit edges')
#     for src, dst in sorted(edges):
    for src, dst in edges:
        dot_lines.append(f'    "{src}" -> "{dst}";')
    for src, color in colors:
        dot_lines.append(f'    "{src}" [color={color}];')

    dot_lines.append('}')

    return '\n'.join(dot_lines)

def main():
    # Example usage
    import sys
    if len(sys.argv) != 2:
        print("Usage: script.py input.txt")
        sys.exit(1)

    filename = sys.argv[1]
    dot_output = process_circuit_file(filename)
    print(dot_output)

if __name__ == "__main__":
    main()