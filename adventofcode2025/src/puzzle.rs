use std::fmt::Display;

pub fn run<R: Display, T: FnOnce(&Vec<String>) -> R>(method: T, input: &Vec<String>) {
    let result = method(input);
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
}
