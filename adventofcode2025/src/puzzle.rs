use std::env;
use std::fmt::Display;

pub fn run<R, M1, M2>(method_a: M1, method_b: M2, year: u16, day: u8)
where
    // In theory we might want different types for A and B.
    // But I can suck it up. It's better if they're the same.
    R: Display,
    M1: FnOnce(&Vec<String>) -> R,
    M2: FnOnce(&Vec<String>) -> R,
{
    let input = crate::input::get_input_for_day(year, day);
    let result: R;

    match env::args().nth(1).as_deref() {
        Some("a") => {
            result = method_a(&input);
        }
        Some("b") => {
            result = method_b(&input);
        }
        _ => {
            panic!("Please specify 'a' or 'b' as the first argument");
        }
    }
    println!("Result: {}", result);
}

pub mod test {
    use crate::input::get_input_from_literal;
    use std::fmt::Debug;

    pub fn run_sample<R: PartialEq + Debug, T: FnOnce(&Vec<String>) -> R>(
        method: T,
        input: &str,
        result: R,
    ) {
        assert_eq!(method(&get_input_from_literal(input)), result);
    }

    pub fn run_input<R: PartialEq + Debug, T: FnOnce(&Vec<String>) -> R>(
        method: T,
        year: u16,
        day: u8,
        result: R,
    ) {
        assert_eq!(method(&crate::input::get_input_for_day(year, day)), result);
    }
}
