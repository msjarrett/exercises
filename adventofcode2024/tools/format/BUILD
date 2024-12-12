load("@aspect_rules_lint//format:defs.bzl", "format_multirun")
#load("@aspect_bazel_lib//tools/build_defs/repo:http.bzl", "http_jar")
#load("@rules_java//java:defs.bzl", "java_binary")

format_multirun(
    name = "format",
    kotlin = ":ktfmt",
    starlark = "@buildifier_prebuilt//:buildifier",
    #markdown = ":prettifier",
    visibility = ["//visibility:public"],
)

java_binary(
    name = "ktfmt",
    main_class = "com.facebook.ktfmt.cli.Main",
    runtime_deps = ["@maven//:com_facebook_ktfmt"],
)
