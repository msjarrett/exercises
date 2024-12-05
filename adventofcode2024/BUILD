load("@aspect_rules_lint//format:defs.bzl", "format_multirun")

# There is a kt_jvm_binary rule, but it generates a bash script, which isn't so helpful on Windows.
java_binary(
    name = "app",
    main_class = "com.knottysoftware.exercises.adventofcode2024.AppKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

java_binary(
    name = "downloader",
    main_class = "com.knottysoftware.exercises.adventofcode.runner.InputDownloaderKt",
    runtime_deps = ["//src/main/kotlin/com/knottysoftware/exercises/adventofcode/runner"],
)

java_test(
    name = "SamplesTest",
    test_class = "com.knottysoftware.exercises.adventofcode2024.SamplesTest",
    runtime_deps = [
        "//src/test/kotlin/com/knottysoftware/exercises/adventofcode2024",
    ],
)

java_test(
    name = "InputsTest",
    test_class = "com.knottysoftware.exercises.adventofcode2024.InputsTest",
    runtime_deps = ["//src/test/kotlin/com/knottysoftware/exercises/adventofcode2024"],
)

# This also does not work on Windows. Urgh.
format_multirun(
    name= "format",
)
