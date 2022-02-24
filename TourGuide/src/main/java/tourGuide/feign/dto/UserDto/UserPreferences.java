package tourGuide.feign.dto.UserDto;

import com.jsoniter.annotation.JsonIgnore;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class UserPreferences {
    private int attractionProximity = Integer.MAX_VALUE;
    private CurrencyUnit currency = Monetary.getCurrency("USD");

    private Money lowerPricePoint = Money.of(0, currency);
    private Money highPricePoint = Money.of(Integer.MAX_VALUE, currency);
    private int tripDuration = 1;
    private int ticketQuantity = 1;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    @JsonIgnore
    public CurrencyUnit getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyUnit currency) {
        this.currency = currency;
    }

    @JsonIgnore
    public Money getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(Money lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    @JsonIgnore
    public Money getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(Money highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public UserPreferences() {
    }

    public UserPreferences(int attractionProximity, CurrencyUnit currency, Money lowerPricePoint, Money highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this.attractionProximity = attractionProximity;
        this.currency = currency;
        this.lowerPricePoint = lowerPricePoint;
        this.highPricePoint = highPricePoint;
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }


    public UserPreferences(int attractionProximity, Money lowerPricePoint, Money highPricePoint, int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this.attractionProximity = attractionProximity;
        this.currency = currency;
        this.lowerPricePoint = lowerPricePoint;
        this.highPricePoint = highPricePoint;
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPreferences)) return false;

        UserPreferences that = (UserPreferences) o;

        if (getAttractionProximity() != that.getAttractionProximity()) return false;
        if (getTripDuration() != that.getTripDuration()) return false;
        if (getTicketQuantity() != that.getTicketQuantity()) return false;
        if (getNumberOfAdults() != that.getNumberOfAdults()) return false;
        if (getNumberOfChildren() != that.getNumberOfChildren()) return false;
        if (getCurrency() != null ? !getCurrency().equals(that.getCurrency()) : that.getCurrency() != null)
            return false;
        if (getLowerPricePoint() != null ? !getLowerPricePoint().equals(that.getLowerPricePoint()) : that.getLowerPricePoint() != null)
            return false;
        return getHighPricePoint() != null ? getHighPricePoint().equals(that.getHighPricePoint()) : that.getHighPricePoint() == null;
    }

    @Override
    public int hashCode() {
        int result = getAttractionProximity();
        result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
        result = 31 * result + (getLowerPricePoint() != null ? getLowerPricePoint().hashCode() : 0);
        result = 31 * result + (getHighPricePoint() != null ? getHighPricePoint().hashCode() : 0);
        result = 31 * result + getTripDuration();
        result = 31 * result + getTicketQuantity();
        result = 31 * result + getNumberOfAdults();
        result = 31 * result + getNumberOfChildren();
        return result;
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
                "attractionProximity=" + attractionProximity +
                ", currency=" + currency +
                ", lowerPricePoint=" + lowerPricePoint +
                ", highPricePoint=" + highPricePoint +
                ", tripDuration=" + tripDuration +
                ", ticketQuantity=" + ticketQuantity +
                ", numberOfAdults=" + numberOfAdults +
                ", numberOfChildren=" + numberOfChildren +
                '}';
    }
}
