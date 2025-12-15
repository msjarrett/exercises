use adventofcode2025::puzzle;
use std::ops::RangeInclusive;

const YEAR: u16 = 2025;
const DAY: u8 = 2;

#[allow(dead_code)]
#[rustfmt::skip]
const SAMPLE: &str =
"11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
1698522-1698528,446443-446449,38593856-38593862,565653-565659,
824824821-824824827,2121212118-2121212124";

fn parse(lines: &Vec<String>) -> Vec<RangeInclusive<u64>> {
    let mut ranges = Vec::new();

    for line in lines.iter() {
        for item in line.split(',') {
            if item.is_empty() {
                continue;
            }
            let parts: Vec<&str> = item.split('-').collect();
            ranges.push(parts[0].parse().unwrap()..=parts[1].parse().unwrap());
        }
    }

    ranges
}

fn is_badnum(num: u64) -> bool {
    let numstr: String = num.to_string();
    if 0 == (numstr.len() % 2) {
        let left = &numstr[..(numstr.len() / 2)];
        let right = &numstr[(numstr.len() / 2)..];
        if left == right {
            return true;
        }
    }

    false
}

fn is_badnum_ex(num: u64) -> bool {
    let numstr: String = num.to_string();
    let strlen = numstr.len();

    'split_size: for splits in 2..=strlen {
        if (strlen % splits) != 0 {
            continue;
        }
        if splits > 2 && (splits % 2) == 0 {
            continue;
        }

        let splitlen = strlen / splits;
        let split_zero = &numstr[0..splitlen];
        for i in 1..splits {
            let split_i = &numstr[i * splitlen..(i + 1) * splitlen];
            if split_i != split_zero {
                continue 'split_size;
            }
        }
        return true;
    }

    false
}

fn day_a(lines: &Vec<String>) -> u64 {
    let ranges = parse(lines);
    let mut badnums: Vec<u64> = Vec::new();
    for range in ranges.iter() {
        for num in range.clone() {
            if is_badnum(num) {
                println!("Found badnum: {}", num);
                badnums.push(num);
            }
        }
    }

    badnums.iter().sum()
}

fn day_b(lines: &Vec<String>) -> u64 {
    let ranges = parse(lines);
    let mut badnums: Vec<u64> = Vec::new();
    for range in ranges.iter() {
        for num in range.clone() {
            if is_badnum_ex(num) {
                println!("Found badnum: {}", num);
                badnums.push(num);
            }
        }
    }

    badnums.iter().sum()
}

fn main() {
    puzzle::run(day_a, day_b, YEAR, DAY);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    #[test]
    fn day2_a_sample() {
        test::run_sample(day_a, SAMPLE, 1227775554);
    }

    #[test]
    fn day2_b_sample() {
        test::run_sample(day_b, SAMPLE, 4174379265);
    }

    #[test]
    fn day2a_input() {
        test::run_input(day_a, YEAR, DAY, 19386344315);
    }

    #[test]
    fn day2_b_input() {
        test::run_input(day_b, YEAR, DAY, 34421651192);
    }
}
