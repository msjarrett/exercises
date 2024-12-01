load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library", "kt_jvm_test")

kt_jvm_library(
    name = "aoc_common_lib",
    srcs = glob(["src/main/kotlin/com/knottysoftware/exercises/adventofcode/**/*.kt"]),
    deps = [
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
    ]
)

kt_jvm_library(
    name = "aoc2024_lib",
    srcs = glob(["src/main/kotlin/com/knottysoftware/exercises/adventofcode2024/**/*.kt"]),
    deps = [
        ":aoc_common_lib",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
    ]
)

kt_jvm_library(
    name = "tests_lib",
    srcs = glob(["src/test/kotlin/com/knottysoftware/exercises/adventofcode2024/**/*.kt"]),
    testonly = True,
    deps = [
        ":aoc_common_lib",
        ":aoc2024_lib",
        "@maven//:junit_junit",
    ],
)

# There is a kt_jvm_binary rule, but it generates a bash script, which isn't so helpful on Windows.
java_binary(
    name = "app",
    main_class = "com.knottysoftware.exercises.adventofcode2024.AppKt",
    runtime_deps = [
        ":aoc2024_lib",
    ],
)

java_binary(
    name = "downloader",
    main_class = "com.knottysoftware.exercises.adventofcode.InputDownloaderKt",
    runtime_deps = [
        ":aoc_common_lib",
    ],
)

java_test(
    name = "SamplesTest",
    runtime_deps = [":tests_lib"],
    test_class = "com.knottysoftware.exercises.adventofcode2024.SamplesTest",
)

java_test(
    name = "InputsTest",
    runtime_deps = [":tests_lib"],
    test_class = "com.knottysoftware.exercises.adventofcode2024.InputsTest",
)
