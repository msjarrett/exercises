use adventofcode2025::puzzle;

const YEAR: u16 = 2025;
const DAY: u8 = 3;

#[allow(dead_code)]
#[rustfmt::skip]
const SAMPLE: &str =
"987654321111111
811111111111119
234234234234278
818181911112111";

fn max_pos(bank: &str) -> usize {
    let mut max_char = '0';
    let mut max_pos: usize = 0;

    for (i, c) in bank.chars().enumerate() {
        if c > max_char {
            max_char = c;
            max_pos = i;
        }
        if c == '9' {
            break;
        }
    }
    max_pos
}

fn val_at(bank: &str, pos: usize) -> u8 {
    bank.chars().nth(pos).unwrap().to_digit(10).unwrap() as u8
}

fn day_a(lines: &Vec<String>) -> u64 {
    lines.iter().map(|line| max_joltage_b(line, 2) as u64).sum()
}

fn max_joltage_b(bank: &str, count: usize) -> u64 {
    let mut positions = vec![0; count];

    for i in 0..count {
        let start_pos = if i == 0 { 0 } else { positions[i - 1] + 1 };
        positions[i] = max_pos(&bank[(start_pos)..(bank.len() - (count - i - 1))]) + start_pos;
    }
    println!("positions: {:?}", positions);
    let mut result: u64 = 0;
    for i in 0..count {
        result = result * 10 + (val_at(bank, positions[i]) as u64);
    }
    result
}

fn day_b(lines: &Vec<String>) -> u64 {
    lines.into_iter().map(|line| max_joltage_b(line, 12)).sum()
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
        test::run_sample(day_a, SAMPLE, 357);
    }

    #[test]
    fn day_b_sample() {
        test::run_sample(day_b, SAMPLE, 3121910778619);
    }

    #[test]
    fn day_a_input() {
        test::run_input(day_a, YEAR, DAY, 17107);
    }

    #[test]
    fn day_b_input() {
        test::run_input(day_b, YEAR, DAY, 169349762274117);
    }
}
