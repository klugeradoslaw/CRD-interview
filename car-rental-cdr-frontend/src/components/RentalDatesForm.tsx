import {FormProvider, useForm, useWatch} from "react-hook-form";
import { Box, Button, Grid } from "@mui/material";
import DateTimePickerController from "./DateTimePickerController.tsx";
import dayjs, { Dayjs } from "dayjs";
import { useNavigate, useSearchParams } from "react-router";
import { useEffect } from "react";

export interface RentalDatesFormValues {
    startDate: Dayjs | null;
    endDate: Dayjs | null;
}

interface RentalDatesFormProps {
    direction?: "column" | "row";
    submitButtonText?: string;
}

export const RentalDatesForm = ({
                                    direction = "row",
                                    submitButtonText = "Search",
                                }: RentalDatesFormProps) => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const startParam = searchParams.get("startDate");
    const endParam = searchParams.get("endDate");

    const methods = useForm<RentalDatesFormValues>({
        defaultValues: {
            startDate: startParam ? dayjs(startParam) : null,
            endDate: endParam ? dayjs(endParam) : null,
        },
    });

    useEffect(() => {
        methods.setValue("startDate", startParam ? dayjs(startParam) : null);
        methods.setValue("endDate", endParam ? dayjs(endParam) : null);
    }, [startParam, endParam, methods]);

    const [startDate, endDate] = useWatch({
        control: methods.control,
        name: ["startDate", "endDate"],
    });

    const isInvalidRange = Boolean(
        startDate &&
        endDate &&
        (startDate?.isAfter(endDate) || startDate?.isSame(endDate))
    );
    const isMissingDates = !startDate || !endDate;
    const disableSubmitButton = isMissingDates || isInvalidRange;

    const onSubmit = (data: RentalDatesFormValues) => {
        if (!data.startDate || !data.endDate) return;

        const startIso = data.startDate.format("YYYY-MM-DDTHH:mm:ss");
        const endIso = data.endDate.format("YYYY-MM-DDTHH:mm:ss");
        const params = new URLSearchParams({ startDate: startIso, endDate: endIso });
        navigate({ pathname: '/available-cars', search: params.toString() });
    };

    const isColumn = direction === "column";

    return (
        <FormProvider {...methods}>
            <Box
                component="form"
                onSubmit={methods.handleSubmit(onSubmit)}
                sx={{
                    p: isColumn ? 0 : 3,
                    backgroundColor: isColumn ? "transparent" : "#1e1e1e",
                    borderRadius: 3,
                    border: isColumn ? "none" : "1px solid #333",
                    mb: isColumn ? 0 : 4,
                    width: "100%",
                }}
            >
                <Grid
                    sx={{
                        display: "flex",
                        flexDirection: { xs: "column", md: direction },
                        gap: 2,
                        alignItems: isColumn ? "stretch" : "center",
                        flex: 1
                    }}
                >
                    <DateTimePickerController name="startDate" label="Rental start" />
                    <DateTimePickerController name="endDate" label="Rental end" />
                    <Grid sx={{ height: "56px", flexGrow: isColumn ? 0 : 1, width: isColumn ? "100%" : "auto" }}>
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            size="large"
                            fullWidth
                            disabled={disableSubmitButton}
                            sx={{ height: "56px", fontWeight: 600 }}
                        >
                            {submitButtonText}
                        </Button>
                    </Grid>
                </Grid>
            </Box>
        </FormProvider>
    );
};

export default RentalDatesForm;