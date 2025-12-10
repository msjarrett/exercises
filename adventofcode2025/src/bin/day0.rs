use adventofcode2025::input;

fn day0a(lines: &Vec<String>) -> u16 {
    lines.len() as u16
}

fn day0b(lines: &Vec<String>) -> u16 {
    (lines.len() * 2) as u16
}



fn main() {
    println!("Hello, World!");
    let file_lines = input::get_input_from_file("day0.txt");
    input::run_puzzle(day0a, &file_lines);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    const SAMPLE: &str =
"foo
bar";

    #[test]
    fn day0a_sample() {
        test::run_sample(day0a, SAMPLE, 2);
    }

    #[test]
    fn day0b_sample() {
        test::run_sample(day0b, SAMPLE, 4);
    }
}