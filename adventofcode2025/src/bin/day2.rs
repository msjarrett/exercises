use adventofcode2025::input;
use adventofcode2025::puzzle;
use std::ops::RangeInclusive;

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
    let lines = input::get_input_from_literal(
        "18623-26004,226779-293422,65855-88510,868-1423,248115026-248337139,903911-926580,97-121,67636417-67796062,24-47,6968-10197,193-242,3769-5052,5140337-5233474,2894097247-2894150301,979582-1016336,502-646,9132195-9191022,266-378,58-91,736828-868857,622792-694076,6767592127-6767717303,2920-3656,8811329-8931031,107384-147042,941220-969217,3-17,360063-562672,7979763615-7979843972,1890-2660,23170346-23308802",
    );

    puzzle::run(day_b, &lines);
}

#[cfg(test)]
mod tests {
    use super::*;
    use adventofcode2025::puzzle::test;

    const SAMPLE: &str = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
1698522-1698528,446443-446449,38593856-38593862,565653-565659,
824824821-824824827,2121212118-2121212124";

    #[test]
    fn day_a_sample() {
        test::run_sample(day_a, SAMPLE, 1227775554);
    }

    #[test]
    fn day_b_sample() {
        test::run_sample(day_b, SAMPLE, 4174379265);
    }
}
