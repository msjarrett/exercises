use adventofcode2025::puzzle;

const YEAR: u16 = 2025;
const DAY: u8 = 0;

#[allow(dead_code)]
#[rustfmt::skip]
const SAMPLE: &str =
"foo
bar";

fn day_a(lines: &Vec<String>) -> u16 {
    lines.len() as u16
}

fn day_b(lines: &Vec<String>) -> u16 {
    (lines.len() * 2) as u16
}

fn main() {
    puzzle::run(day_a, day_b, YEAR, DAY);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    #[test]
    fn day_a_sample() {
        test::run_sample(day_a, SAMPLE, 2);
    }

    #[test]
    fn day_b_sample() {
        test::run_sample(day_b, SAMPLE, 4);
    }

    #[test]
    fn day_a_input() {
        test::run_input(day_a, YEAR, DAY, 2);
    }

    #[test]
    fn day_b_input() {
        test::run_input(day_b, YEAR, DAY, 4);
    }
}
