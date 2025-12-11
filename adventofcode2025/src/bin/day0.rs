use adventofcode2025::input;
use adventofcode2025::puzzle;

fn day_a(lines: &Vec<String>) -> u16 {
    lines.len() as u16
}

fn day_b(lines: &Vec<String>) -> u16 {
    (lines.len() * 2) as u16
}

fn main() {
    println!("Hello, World!");
    let lines = input::get_input_from_file("day0.txt");
    //let lines = input::get_input_from_literal("foo bar");
    puzzle::run(day_a, day_b, &lines);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    #[rustfmt::skip]
    const SAMPLE: &str =
"foo
bar";

    #[test]
    fn day_a_sample() {
        test::run_sample(day_a, SAMPLE, 2);
    }

    #[test]
    fn day_b_sample() {
        test::run_sample(day_b, SAMPLE, 4);
    }
}
